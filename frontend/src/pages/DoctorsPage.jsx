import { useEffect, useMemo, useState } from 'react';
import toast from 'react-hot-toast';
import api from '../api/client';
import DataTable from '../components/DataTable';
import { useAuth } from '../context/AuthContext';

const initialForm = {
  fullName: '',
  email: '',
  phone: '',
  specialty: '',
  licenseNumber: '',
  yearsOfExperience: 0,
  availability: '',
  active: true,
};

export default function DoctorsPage() {
  const { user } = useAuth();
  const [doctors, setDoctors] = useState([]);
  const [query, setQuery] = useState('');
  const [form, setForm] = useState(initialForm);

  const load = () => {
    const endpoint = user?.role === 'ADMIN' ? '/doctors' : '/doctors';
    api.get(endpoint)
      .then(({ data }) => setDoctors(data))
      .catch(() => toast.error('Unable to load doctors'));
  };

  useEffect(() => {
    load();
  }, []);

  const filtered = useMemo(
    () => doctors.filter((doctor) => `${doctor.fullName} ${doctor.specialty} ${doctor.email}`.toLowerCase().includes(query.toLowerCase())),
    [doctors, query],
  );

  const submit = async (event) => {
    event.preventDefault();
    try {
      await api.post('/doctors', form);
      toast.success('Doctor created');
      setForm(initialForm);
      load();
    } catch (error) {
      toast.error(error?.response?.data?.message || 'Unable to create doctor');
    }
  };

  const removeDoctor = async (id) => {
    try {
      await api.delete(`/doctors/${id}`);
      toast.success('Doctor deleted');
      load();
    } catch {
      toast.error('Delete failed');
    }
  };

  const columns = [
    { key: 'fullName', label: 'Name' },
    { key: 'specialty', label: 'Specialty' },
    { key: 'licenseNumber', label: 'License' },
    { key: 'yearsOfExperience', label: 'Experience' },
    { key: 'availability', label: 'Availability' },
    { key: 'email', label: 'Email' },
    { key: 'actions', label: 'Actions', render: (row) => user?.role === 'ADMIN' ? <button className="btn-secondary py-2" onClick={() => removeDoctor(row.id)} type="button">Delete</button> : 'View only' },
  ];

  return (
    <div className="space-y-6">
      <div className="glass-card p-6">
        <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
          <div>
            <p className="text-sm text-slate-400">Clinical staff</p>
            <h2 className="text-2xl font-semibold text-white">Doctors directory</h2>
          </div>
          <input className="input-field max-w-sm" placeholder="Search doctors" value={query} onChange={(event) => setQuery(event.target.value)} />
        </div>
      </div>

      {user?.role === 'ADMIN' && (
        <form className="glass-card grid gap-4 p-6 lg:grid-cols-3" onSubmit={submit}>
          <input className="input-field" placeholder="Full name" value={form.fullName} onChange={(event) => setForm({ ...form, fullName: event.target.value })} />
          <input className="input-field" placeholder="Email" value={form.email} onChange={(event) => setForm({ ...form, email: event.target.value })} />
          <input className="input-field" placeholder="Phone" value={form.phone} onChange={(event) => setForm({ ...form, phone: event.target.value })} />
          <input className="input-field" placeholder="Specialty" value={form.specialty} onChange={(event) => setForm({ ...form, specialty: event.target.value })} />
          <input className="input-field" placeholder="License number" value={form.licenseNumber} onChange={(event) => setForm({ ...form, licenseNumber: event.target.value })} />
          <input className="input-field" placeholder="Years of experience" type="number" value={form.yearsOfExperience} onChange={(event) => setForm({ ...form, yearsOfExperience: Number(event.target.value) })} />
          <input className="input-field lg:col-span-3" placeholder="Availability (e.g. Mon-Fri 9:00 AM - 5:00 PM)" value={form.availability} onChange={(event) => setForm({ ...form, availability: event.target.value })} />
          <button className="btn-primary lg:col-span-3" type="submit">Add doctor</button>
        </form>
      )}

      <DataTable columns={columns} rows={filtered} emptyText="No doctors available" />
    </div>
  );
}