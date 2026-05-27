import { useEffect, useMemo, useState } from 'react';
import toast from 'react-hot-toast';
import api from '../api/client';
import DataTable from '../components/DataTable';
import { useAuth } from '../context/AuthContext';

const initialForm = {
  patientId: '',
  doctorId: '',
  medicationName: '',
  dosage: '',
  frequency: '',
  duration: '',
  notes: '',
};

export default function PrescriptionsPage() {
  const { user } = useAuth();
  const [prescriptions, setPrescriptions] = useState([]);
  const [patients, setPatients] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [query, setQuery] = useState('');
  const [form, setForm] = useState(initialForm);

  useEffect(() => {
    const prescriptionEndpoint = user?.role === 'PATIENT' || user?.role === 'DOCTOR' ? '/prescriptions/my' : '/prescriptions';

    const patientPromise = user?.role === 'ADMIN'
      ? api.get('/patients')
      : user?.role === 'DOCTOR'
        ? api.get('/doctors/me/patients')
        : Promise.resolve({ data: [] });

    const doctorPromise = user?.role === 'ADMIN' || user?.role === 'DOCTOR'
      ? api.get('/doctors')
      : Promise.resolve({ data: [] });

    Promise.all([
      api.get(prescriptionEndpoint),
      patientPromise,
      doctorPromise,
    ])
      .then(([prescriptionRes, patientRes, doctorRes]) => {
        setPrescriptions(prescriptionRes.data);
        setPatients(patientRes.data);
        setDoctors(doctorRes.data);

        // If current user is a doctor, preselect their doctorId in the form
        if (user?.role === 'DOCTOR' && Array.isArray(doctorRes.data)) {
          const myDoctor = doctorRes.data.find((d) => d.userId === user.userId);
          if (myDoctor) setForm((prev) => ({ ...prev, doctorId: String(myDoctor.id) }));
        }
      })
      .catch(() => toast.error('Unable to load prescriptions'));
  }, [user]);

  const filtered = useMemo(
    () => prescriptions.filter((item) => `${item.patientName} ${item.doctorName} ${item.medicationName}`.toLowerCase().includes(query.toLowerCase())),
    [prescriptions, query],
  );

  const submit = async (event) => {
    event.preventDefault();
    try {
      await api.post('/prescriptions', {
        ...form,
        patientId: Number(form.patientId),
        doctorId: Number(form.doctorId),
      });
      toast.success('Prescription added');
      setForm(initialForm);
      const { data } = await api.get(user?.role === 'PATIENT' || user?.role === 'DOCTOR' ? '/prescriptions/my' : '/prescriptions');
      setPrescriptions(data);
    } catch {
      toast.error('Unable to save prescription');
    }
  };

  const columns = [
    { key: 'patientName', label: 'Patient' },
    { key: 'doctorName', label: 'Doctor' },
    { key: 'medicationName', label: 'Medication' },
    { key: 'dosage', label: 'Dosage' },
    { key: 'frequency', label: 'Frequency' },
    { key: 'prescribedDate', label: 'Date', render: (row) => new Date(row.prescribedDate).toLocaleDateString() },
  ];

  return (
    <div className="space-y-6">
      <div className="glass-card p-6">
        <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <p className="text-sm text-slate-400">Medication tracking</p>
            <h2 className="text-2xl font-semibold text-white">Prescriptions</h2>
          </div>
          <input className="input-field max-w-sm" placeholder="Search prescriptions" value={query} onChange={(event) => setQuery(event.target.value)} />
        </div>
      </div>

      {(user?.role === 'ADMIN' || user?.role === 'DOCTOR') && (
        <form className="glass-card grid gap-4 p-6 lg:grid-cols-2" onSubmit={submit}>
          <select className="input-field" value={form.patientId} onChange={(event) => setForm({ ...form, patientId: event.target.value })}>
            <option value="">Select patient</option>
            {patients.map((patient) => <option key={patient.id} value={patient.id}>{patient.fullName}</option>)}
          </select>
          {user?.role === 'DOCTOR' ? (
            // Show only current doctor and disable changing it for doctors
            (() => {
              const myDoctor = doctors.find((d) => d.userId === user.userId) || doctors[0] || null;
              return (
                <select className="input-field" value={form.doctorId} disabled>
                  <option value={myDoctor ? String(myDoctor.id) : ''}>{myDoctor ? myDoctor.fullName : 'Doctor'}</option>
                </select>
              );
            })()
          ) : (
            <select className="input-field" value={form.doctorId} onChange={(event) => setForm({ ...form, doctorId: event.target.value })}>
              <option value="">Select doctor</option>
              {doctors.map((doctor) => <option key={doctor.id} value={doctor.id}>{doctor.fullName}</option>)}
            </select>
          )}
          <input className="input-field" placeholder="Medication" value={form.medicationName} onChange={(event) => setForm({ ...form, medicationName: event.target.value })} />
          <input className="input-field" placeholder="Dosage" value={form.dosage} onChange={(event) => setForm({ ...form, dosage: event.target.value })} />
          <input className="input-field" placeholder="Frequency" value={form.frequency} onChange={(event) => setForm({ ...form, frequency: event.target.value })} />
          <input className="input-field" placeholder="Duration" value={form.duration} onChange={(event) => setForm({ ...form, duration: event.target.value })} />
          <input className="input-field lg:col-span-2" placeholder="Notes" value={form.notes} onChange={(event) => setForm({ ...form, notes: event.target.value })} />
          <button className="btn-primary lg:col-span-2" type="submit">Add prescription</button>
        </form>
      )}

      <DataTable columns={columns} rows={filtered} emptyText="No prescriptions available" />
    </div>
  );
}