import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import api from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function ProfilePage() {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [form, setForm] = useState({
    fullName: '',
    email: '',
    phone: '',
    dateOfBirth: '',
    gender: 'MALE',
    bloodGroup: '',
    address: '',
    emergencyContact: '',
  });

  useEffect(() => {
    const requests = [api.get('/auth/me')];

    if (user?.role === 'PATIENT') {
      requests.push(api.get('/patients/me'));
    }

    Promise.all(requests)
      .then(([authResponse, patientResponse]) => {
        setProfile(authResponse.data);

        if (patientResponse?.data) {
          const patient = patientResponse.data;
          setForm({
            fullName: patient.fullName || '',
            email: patient.email || '',
            phone: patient.phone || '',
            dateOfBirth: patient.dateOfBirth || '',
            gender: patient.gender || 'MALE',
            bloodGroup: patient.bloodGroup || '',
            address: patient.address || '',
            emergencyContact: patient.emergencyContact || '',
          });
        }
      })
      .catch(() => toast.error('Unable to load profile'));
  }, [user]);

  const submit = async (event) => {
    event.preventDefault();
    try {
      await api.put('/patients/me', form);
      toast.success('Profile updated');
    } catch (error) {
      toast.error(error?.response?.data?.message || 'Unable to update profile');
    }
  };

  return (
    <div className="grid gap-6 xl:grid-cols-[0.7fr_1.3fr]">
      <div className="glass-card p-6">
        <p className="text-sm text-slate-400">Account</p>
        <h2 className="text-2xl font-semibold text-white">{user?.fullName}</h2>
        <div className="mt-6 space-y-3 text-sm text-slate-300">
          <div className="flex justify-between rounded-2xl border border-white/10 bg-white/5 px-4 py-3"><span>Email</span><span>{user?.email}</span></div>
          <div className="flex justify-between rounded-2xl border border-white/10 bg-white/5 px-4 py-3"><span>Role</span><span>{user?.role}</span></div>
        </div>
      </div>

      <div className="glass-card p-6">
        <p className="text-sm text-slate-400">Profile data</p>
        <h3 className="text-xl font-semibold text-white">Role-specific information</h3>
        {user?.role === 'PATIENT' ? (
          <form className="mt-4 grid gap-4 lg:grid-cols-2" onSubmit={submit}>
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
            <input className="input-field lg:col-span-2" placeholder="Address" value={form.address} onChange={(event) => setForm({ ...form, address: event.target.value })} />
            <input className="input-field lg:col-span-2" placeholder="Emergency contact" value={form.emergencyContact} onChange={(event) => setForm({ ...form, emergencyContact: event.target.value })} />
            <button className="btn-primary lg:col-span-2" type="submit">Update profile</button>
          </form>
        ) : (
          <pre className="mt-4 overflow-auto rounded-3xl border border-white/10 bg-slate-950/70 p-4 text-xs text-slate-300">{JSON.stringify(profile, null, 2)}</pre>
        )}
      </div>
    </div>
  );
}