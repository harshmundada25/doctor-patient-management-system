import { useEffect, useMemo, useState } from 'react';
import { ResponsiveContainer, PieChart, Pie, Cell, Tooltip, LineChart, Line, XAxis, YAxis, CartesianGrid } from 'recharts';
import toast from 'react-hot-toast';
import api from '../api/client';
import StatCard from '../components/StatCard';

const COLORS = ['#22d3ee', '#34d399', '#f59e0b', '#fb7185'];

export default function DashboardPage() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    api.get('/dashboard/stats')
      .then(({ data }) => setStats(data))
      .catch(() => toast.error('Unable to load dashboard metrics'))
      .finally(() => setLoading(false));
  }, []);

  const chartData = useMemo(() => [
    { name: 'Pending', value: stats?.pendingAppointments || 0 },
    { name: 'Completed', value: stats?.completedAppointments || 0 },
    { name: 'Cancelled', value: stats?.cancelledAppointments || 0 },
  ], [stats]);

  const trendData = useMemo(
    () => (stats?.recentAppointments || []).map((item, index) => ({
      name: item.patientName?.split(' ')[0] || `Visit ${index + 1}`,
      value: index + 1,
    })),
    [stats],
  );

  return (
    <div className="space-y-6">
      <section className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        <StatCard title="Doctors" value={loading ? '...' : stats?.doctorCount ?? '0'} helper="Active network" tone="cyan" />
        <StatCard title="Patients" value={loading ? '...' : stats?.patientCount ?? '0'} helper="Registered users" tone="emerald" />
        <StatCard title="Appointments" value={loading ? '...' : stats?.appointmentCount ?? '0'} helper="Workflow volume" tone="amber" />
        <StatCard title="Medical records" value={loading ? '...' : stats?.medicalRecordCount ?? '0'} helper="Clinical history" tone="rose" />
      </section>

      <section className="grid gap-4 md:grid-cols-3">
        <StatCard title="Pending" value={loading ? '...' : stats?.pendingAppointments ?? '0'} helper="Needs action" tone="amber" />
        <StatCard title="Completed" value={loading ? '...' : stats?.completedAppointments ?? '0'} helper="Closed visits" tone="emerald" />
        <StatCard title="Cancelled" value={loading ? '...' : stats?.cancelledAppointments ?? '0'} helper="Dropped visits" tone="rose" />
      </section>

      <section className="grid gap-6 xl:grid-cols-[1.1fr_0.9fr]">
        <div className="glass-card p-6">
          <div className="mb-6 flex items-center justify-between">
            <div>
              <p className="text-sm text-slate-400">Operational health</p>
              <h3 className="text-xl font-semibold text-white">Appointment status mix</h3>
            </div>
          </div>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie data={chartData} dataKey="value" innerRadius={70} outerRadius={110} paddingAngle={4}>
                  {chartData.map((entry, index) => <Cell key={entry.name} fill={COLORS[index % COLORS.length]} />)}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="glass-card p-6">
          <p className="text-sm text-slate-400">Recent activity</p>
          <h3 className="text-xl font-semibold text-white">Latest appointments</h3>
          <div className="mt-6 h-80">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={trendData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" />
                <XAxis dataKey="name" stroke="#94a3b8" />
                <YAxis stroke="#94a3b8" />
                <Tooltip />
                <Line type="monotone" dataKey="value" stroke="#22d3ee" strokeWidth={3} dot={{ r: 4 }} />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>
      </section>

      <section className="glass-card p-6">
        <h3 className="text-xl font-semibold text-white">Recent appointments</h3>
        <div className="mt-4 grid gap-3 md:grid-cols-2 xl:grid-cols-3">
          {loading ? (
            Array.from({ length: 3 }).map((_, index) => (
              <div key={index} className="animate-pulse rounded-3xl border border-white/10 bg-white/5 p-4">
                <div className="h-4 w-1/2 rounded-full bg-white/10" />
                <div className="mt-3 h-3 w-1/3 rounded-full bg-white/10" />
                <div className="mt-4 h-3 w-2/3 rounded-full bg-white/10" />
              </div>
            ))
          ) : (stats?.recentAppointments || []).length ? (stats.recentAppointments.map((appointment) => (
            <div key={appointment.id} className="rounded-3xl border border-white/10 bg-white/5 p-4">
              <p className="text-sm font-semibold text-white">{appointment.patientName}</p>
              <p className="mt-1 text-sm text-slate-400">{appointment.doctorName}</p>
              <div className="mt-3 flex items-center justify-between text-xs text-slate-300">
                <span>{new Date(appointment.appointmentDateTime).toLocaleString()}</span>
                <span className="rounded-full border border-white/10 bg-white/5 px-3 py-1">{appointment.status}</span>
              </div>
            </div>
          ))) : (
            <p className="text-sm text-slate-400">No recent appointments yet.</p>
          )}
        </div>
      </section>
    </div>
  );
}