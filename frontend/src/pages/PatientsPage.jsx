import { useEffect, useMemo, useState } from 'react';
import toast from 'react-hot-toast';
import api from '../api/client';
import DataTable from '../components/DataTable';
import { useAuth } from '../context/AuthContext';

const initialForm = {
  fullName: '',
  email: '',
  phone: '',
  dateOfBirth: '',
  gender: 'MALE',
  bloodGroup: '',
  address: '',
  emergencyContact: '',
  password: '',
  assignedDoctorId: '',
};

export default function PatientsPage() {
  const { user } = useAuth();
  const [patients, setPatients] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [query, setQuery] = useState('');
  const [form, setForm] = useState(initialForm);

  useEffect(() => {
    const endpoint = user?.role === 'DOCTOR' ? '/doctors/me/patients' : '/patients';
    const doctorsRequest = api.get('/doctors');

    Promise.all([
      api.get(endpoint),
      doctorsRequest,
    ])
      .then(([patientsResponse, doctorsResponse]) => {
        setPatients(patientsResponse.data);
        setDoctors(doctorsResponse.data);
      })
      .catch(() => toast.error('Unable to load patients'));
  }, [user]);

  const filtered = useMemo(
    () => patients.filter((patient) => `${patient.fullName} ${patient.bloodGroup} ${patient.email}`.toLowerCase().includes(query.toLowerCase())),
    [patients, query],
  );

  const columns = [
    { key: 'fullName', label: 'Name' },
    { key: 'bloodGroup', label: 'Blood Group' },
    { key: 'gender', label: 'Gender' },
    { key: 'email', label: 'Email' },
    { key: 'assignedDoctorName', label: 'Assigned Doctor' },
  ];

  const submit = async (event) => {
    event.preventDefault();
    try {
      await api.post('/patients', {
        ...form,
        assignedDoctorId: form.assignedDoctorId ? Number(form.assignedDoctorId) : null,
      });
      toast.success('Patient created');
      setForm(initialForm);
      const { data } = await api.get(user?.role === 'DOCTOR' ? '/doctors/me/patients' : '/patients');
      setPatients(data);
    } catch (error) {
      toast.error(error?.response?.data?.message || 'Unable to create patient');
    }
  };

  return (
    <div className="space-y-6">
      <div className="glass-card p-6">
        <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <p className="text-sm text-slate-400">Patient registry</p>
            <h2 className="text-2xl font-semibold text-white">Patients directory</h2>
          </div>
          <input className="input-field max-w-sm" placeholder="Search patients" value={query} onChange={(event) => setQuery(event.target.value)} />
        </div>
      </div>

      {user?.role === 'ADMIN' && (
        <form className="glass-card grid gap-4 p-6 lg:grid-cols-2 xl:grid-cols-3" onSubmit={submit}>
          <input className="input-field" placeholder="Full name" value={form.fullName} onChange={(event) => setForm({ ...form, fullName: event.target.value })} />
          <input className="input-field" placeholder="Email" value={form.email} onChange={(event) => setForm({ ...form, email: event.target.value })} />
          <input className="input-field" placeholder="Phone" value={form.phone} onChange={(event) => setForm({ ...form, phone: event.target.value })} />
          <input className="input-field" type="date" value={form.dateOfBirth} onChange={(event) => setForm({ ...form, dateOfBirth: event.target.value })} />
          <select className="input-field" value={form.gender} onChange={(event) => setForm({ ...form, gender: event.target.value })}>
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
            <option value="OTHER">Other</option>
          </select>
          <input className="input-field" placeholder="Blood group" value={form.bloodGroup} onChange={(event) => setForm({ ...form, bloodGroup: event.target.value })} />
          <input className="input-field xl:col-span-2" placeholder="Address" value={form.address} onChange={(event) => setForm({ ...form, address: event.target.value })} />
          <input className="input-field" placeholder="Emergency contact" value={form.emergencyContact} onChange={(event) => setForm({ ...form, emergencyContact: event.target.value })} />
          <input className="input-field" placeholder="Password" type="password" value={form.password} onChange={(event) => setForm({ ...form, password: event.target.value })} />
          <select className="input-field" value={form.assignedDoctorId} onChange={(event) => setForm({ ...form, assignedDoctorId: event.target.value })}>
            <option value="">Assign doctor</option>
            {doctors.map((doctor) => <option key={doctor.id} value={doctor.id}>{doctor.fullName}</option>)}
          </select>
          <button className="btn-primary lg:col-span-2 xl:col-span-3" type="submit">Add patient</button>
        </form>
      )}

      <DataTable columns={columns} rows={filtered} emptyText="No patient records found" />
    </div>
  );
}