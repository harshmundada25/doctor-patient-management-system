import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import api from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function RegisterPage() {
  const [form, setForm] = useState({ fullName: '', email: '', password: '', phone: '' });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const submit = async (event) => {
    event.preventDefault();
    setLoading(true);
    try {
      const { data } = await api.post('/auth/register', form);
      login(data);
      toast.success('Account created');
      navigate('/dashboard');
    } catch (error) {
      toast.error(error?.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="grid min-h-screen lg:grid-cols-[0.9fr_1.1fr]">
      <div className="hidden bg-[radial-gradient(circle_at_bottom_right,_rgba(16,185,129,0.18),_transparent_30%),linear-gradient(135deg,_#020617,_#111827)] p-10 lg:flex lg:flex-col lg:justify-between">
        <div>
          <p className="text-sm uppercase tracking-[0.4em] text-emerald-300">Patient onboarding</p>
          <h1 className="mt-4 max-w-xl text-5xl font-semibold leading-tight text-white">Register to book appointments and view your care timeline.</h1>
        </div>
        <div className="glass-card max-w-lg p-6 text-slate-200">
          <p className="text-sm text-slate-400">New patients get secure JWT access, medical history visibility, prescription tracking, and appointment booking.</p>
        </div>
      </div>

      <div className="flex items-center justify-center bg-hospital-radial px-6 py-12">
        <div className="glass-card w-full max-w-md p-8">
          <p className="text-xs uppercase tracking-[0.4em] text-emerald-300">Create account</p>
          <h2 className="mt-2 text-3xl font-semibold text-white">Patient registration</h2>
          <form className="mt-8 space-y-4" onSubmit={submit}>
            <input className="input-field" placeholder="Full name" value={form.fullName} onChange={(event) => setForm({ ...form, fullName: event.target.value })} />
            <input className="input-field" placeholder="Email" type="email" value={form.email} onChange={(event) => setForm({ ...form, email: event.target.value })} />
            <input className="input-field" placeholder="Password" type="password" value={form.password} onChange={(event) => setForm({ ...form, password: event.target.value })} />
            <input className="input-field" placeholder="Phone" value={form.phone} onChange={(event) => setForm({ ...form, phone: event.target.value })} />
            <button className="btn-primary w-full" type="submit" disabled={loading}>{loading ? 'Creating account...' : 'Register'}</button>
          </form>
          <p className="mt-5 text-sm text-slate-400">
            Already registered? <Link className="text-emerald-300 hover:text-emerald-200" to="/login">Login here</Link>
          </p>
        </div>
      </div>
    </div>
  );
}