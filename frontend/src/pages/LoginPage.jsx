import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import api from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
  const [form, setForm] = useState({ email: 'admin@hospital.com', password: 'Admin@123' });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();
  const [role, setRole] = useState('patient');

  const submit = async (event) => {
    event.preventDefault();
    setLoading(true);
    try {
      const { data } = await api.post('/auth/login', form);
      login(data);
      toast.success('Welcome back');
      navigate('/dashboard');
    } catch (error) {
      toast.error(error?.response?.data?.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="grid min-h-screen lg:grid-cols-[1.1fr_0.9fr]">
      <div className="hidden flex-col justify-between bg-[radial-gradient(circle_at_top_left,_rgba(14,165,233,0.25),_transparent_30%),linear-gradient(135deg,_#020617,_#0f172a)] p-10 lg:flex">
        <div>
          <p className="text-sm uppercase tracking-[0.4em] text-cyan-300">Apex Care</p>
          <h1 className="mt-4 max-w-xl text-5xl font-semibold leading-tight text-white">Modern healthcare operations with secure role-based access.</h1>
        </div>
        <div className="glass-card max-w-lg p-6 text-slate-200">
          <p className="text-sm text-slate-400">Includes JWT auth, doctor/patient workflows, medical records, appointment management, and production-ready APIs.</p>
        </div>
      </div>

      <div className="flex items-center justify-center bg-hospital-radial px-6 py-12">
        <div className="glass-card w-full max-w-md p-8">
          <p className="text-xs uppercase tracking-[0.4em] text-cyan-300">Sign in</p>
          <h2 className="mt-2 text-3xl font-semibold text-white">Access the dashboard</h2>
          <form className="mt-8 space-y-4" onSubmit={submit}>
            <div className="flex items-center gap-3">
              <button type="button" onClick={() => setRole('patient')} className={`rounded-full px-3 py-1 text-sm ${role === 'patient' ? 'bg-cyan-400 text-slate-900' : 'bg-white/5 text-slate-300'}`}>
                Patient
              </button>
              <button type="button" onClick={() => setRole('doctor')} className={`rounded-full px-3 py-1 text-sm ${role === 'doctor' ? 'bg-cyan-400 text-slate-900' : 'bg-white/5 text-slate-300'}`}>
                Doctor
              </button>
              <p className="ml-auto text-xs text-slate-400">Signing in as: <span className="text-slate-200">{role}</span></p>
            </div>
            <div>
              <label className="mb-2 block text-sm text-slate-300">Email</label>
              <input className="input-field" value={form.email} onChange={(event) => setForm({ ...form, email: event.target.value })} type="email" />
            </div>
            <div>
              <label className="mb-2 block text-sm text-slate-300">Password</label>
              <input className="input-field" value={form.password} onChange={(event) => setForm({ ...form, password: event.target.value })} type="password" />
            </div>
            <button className="btn-primary w-full" type="submit" disabled={loading}>
              {loading ? 'Signing in...' : 'Login'}
            </button>
          </form>
          <p className="mt-5 text-sm text-slate-400">
            {role === 'patient' ? (
              <>New patient? <Link className="text-cyan-300 hover:text-cyan-200" to="/register">Create an account</Link></>
            ) : (
              <>Doctor access is managed by the admin. <a className="text-cyan-300 hover:text-cyan-200" href="mailto:admin@hospital.com">Request access</a></>
            )}
          </p>
          <div className="mt-6 rounded-2xl border border-white/10 bg-white/5 p-4 text-xs text-slate-400">
            Demo logins: admin@hospital.com / Admin@123, plus seeded doctor and patient accounts listed in the README.
          </div>
        </div>
      </div>
    </div>
  );
}