import { useEffect, useMemo, useState } from 'react';
import toast from 'react-hot-toast';
import api from '../api/client';
import DataTable from '../components/DataTable';
import { useAuth } from '../context/AuthContext';

const initialForm = {
  patientId: '',
  doctorId: '',
  appointmentDateTime: '',
  reason: '',
  notes: '',
};

export default function AppointmentsPage() {
  const { user } = useAuth();
  const [appointments, setAppointments] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [patients, setPatients] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [query, setQuery] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    const appointmentEndpoint = user?.role === 'ADMIN' ? '/appointments' : '/appointments/my';
    Promise.all([
      api.get(appointmentEndpoint),
      api.get('/doctors'),
      user?.role === 'ADMIN' ? api.get('/patients') : user?.role === 'PATIENT' ? api.get('/patients/me').then((response) => ({ data: [response.data] })) : Promise.resolve({ data: [] }),
    ])
      .then(([appointmentRes, doctorRes, patientRes]) => {
        setAppointments(appointmentRes.data);
        setDoctors(doctorRes.data);
        setPatients(patientRes.data);
      })
      .catch(() => toast.error('Unable to load appointments'))
      .finally(() => setLoading(false));
  }, [user]);

  const filtered = useMemo(
    () => appointments.filter((appointment) => `${appointment.patientName} ${appointment.doctorName} ${appointment.reason}`.toLowerCase().includes(query.toLowerCase())),
    [appointments, query],
  );

  const submit = async (event) => {
    event.preventDefault();
    try {
      const payload = {
        ...form,
        patientId: Number(form.patientId),
        doctorId: Number(form.doctorId),
      };

      if (editingId) {
        await api.put(`/appointments/${editingId}`, payload);
        toast.success('Appointment updated');
      } else {
        await api.post('/appointments', payload);
        toast.success('Appointment booked');
      }

      setForm(initialForm);
      setEditingId(null);
      const { data } = await api.get(user?.role === 'ADMIN' ? '/appointments' : '/appointments/my');
      setAppointments(data);
    } catch (error) {
      toast.error(error?.response?.data?.message || 'Unable to save appointment');
    }
  };

  const editAppointment = (appointment) => {
    setEditingId(appointment.id);
    setForm({
      patientId: String(appointment.patientId),
      doctorId: String(appointment.doctorId),
      appointmentDateTime: appointment.appointmentDateTime?.slice(0, 16) || '',
      reason: appointment.reason || '',
      notes: appointment.notes || '',
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const cancelAppointment = async (id) => {
    try {
      await api.patch(`/appointments/${id}/status`, null, { params: { status: 'CANCELLED' } });
      toast.success('Appointment cancelled');
      const { data } = await api.get(user?.role === 'ADMIN' ? '/appointments' : '/appointments/my');
      setAppointments(data);
    } catch {
      toast.error('Unable to cancel appointment');
    }
  };

  const updateStatus = async (id, status) => {
    try {
      await api.patch(`/appointments/${id}/status`, null, { params: { status } });
      toast.success('Status updated');
      const { data } = await api.get(user?.role === 'ADMIN' ? '/appointments' : '/appointments/my');
      setAppointments(data);
    } catch {
      toast.error('Status update failed');
    }
  };

  const columns = [
    { key: 'patientName', label: 'Patient' },
    { key: 'doctorName', label: 'Doctor' },
    { key: 'appointmentDateTime', label: 'Date/Time', render: (row) => new Date(row.appointmentDateTime).toLocaleString() },
    { key: 'status', label: 'Status', render: (row) => <span className="rounded-full border border-white/10 bg-white/5 px-3 py-1 text-xs">{row.status}</span> },
    { key: 'reason', label: 'Reason' },
    { key: 'actions', label: 'Actions', render: (row) => user?.role !== 'PATIENT' ? (
      <div className="flex flex-wrap gap-2">
        <button className="btn-secondary py-2" onClick={() => editAppointment(row)} type="button">Edit</button>
        <button className="btn-secondary py-2 text-rose-300" onClick={() => cancelAppointment(row.id)} type="button">Cancel</button>
        <select className="rounded-xl border border-white/10 bg-slate-900 px-3 py-2 text-sm" defaultValue={row.status} onChange={(event) => updateStatus(row.id, event.target.value)}>
          <option value="PENDING">PENDING</option>
          <option value="CONFIRMED">CONFIRMED</option>
          <option value="COMPLETED">COMPLETED</option>
          <option value="CANCELLED">CANCELLED</option>
        </select>
      </div>
    ) : 'View only' },
  ];

  return (
    <div className="space-y-6">
      <div className="glass-card p-6">
        <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <p className="text-sm text-slate-400">Care coordination</p>
            <h2 className="text-2xl font-semibold text-white">Appointments</h2>
          </div>
          <input className="input-field max-w-sm" placeholder="Search appointments" value={query} onChange={(event) => setQuery(event.target.value)} />
        </div>
      </div>

      {(user?.role === 'ADMIN' || user?.role === 'PATIENT') && (
        <form className="glass-card grid gap-4 p-6 lg:grid-cols-2" onSubmit={submit}>
          <div className="lg:col-span-2 flex items-center justify-between">
            <div>
              <p className="text-sm text-slate-400">{editingId ? 'Edit appointment' : 'New booking'}</p>
              <h3 className="text-xl font-semibold text-white">{editingId ? 'Update appointment details' : 'Book an appointment'}</h3>
            </div>
            {editingId && (
              <button className="btn-secondary" type="button" onClick={() => { setEditingId(null); setForm(initialForm); }}>
                Clear edit
              </button>
            )}
          </div>
          {user?.role === 'ADMIN' ? (
            <select className="input-field" value={form.patientId} onChange={(event) => setForm({ ...form, patientId: event.target.value })}>
              <option value="">Select patient</option>
              {patients.map((patient) => <option key={patient.id} value={patient.id}>{patient.fullName}</option>)}
            </select>
          ) : (
            <input className="input-field" value={patients[0]?.fullName || 'Loading your profile...'} disabled />
          )}
          <select className="input-field" value={form.doctorId} onChange={(event) => setForm({ ...form, doctorId: event.target.value })}>
            <option value="">Select doctor</option>
            {doctors.map((doctor) => <option key={doctor.id} value={doctor.id}>{doctor.fullName}</option>)}
          </select>
          <input className="input-field" type="datetime-local" value={form.appointmentDateTime} onChange={(event) => setForm({ ...form, appointmentDateTime: event.target.value })} />
          <input className="input-field" placeholder="Reason" value={form.reason} onChange={(event) => setForm({ ...form, reason: event.target.value })} />
          <input className="input-field lg:col-span-2" placeholder="Notes" value={form.notes} onChange={(event) => setForm({ ...form, notes: event.target.value })} />
          <button className="btn-primary lg:col-span-2" type="submit">{editingId ? 'Save changes' : 'Book appointment'}</button>
        </form>
      )}

      <DataTable columns={columns} rows={filtered} emptyText="No appointments found" loading={loading} pageSize={6} />
    </div>
  );
}