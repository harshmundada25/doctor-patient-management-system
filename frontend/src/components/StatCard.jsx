export default function StatCard({ title, value, helper, tone = 'cyan' }) {
  const toneClasses = {
    cyan: 'from-cyan-400/20 to-cyan-400/5 text-cyan-300',
    emerald: 'from-emerald-400/20 to-emerald-400/5 text-emerald-300',
    amber: 'from-amber-400/20 to-amber-400/5 text-amber-300',
    rose: 'from-rose-400/20 to-rose-400/5 text-rose-300',
  };

  return (
    <div className={`glass-card bg-gradient-to-br p-5 ${toneClasses[tone]}`}>
      <p className="text-sm text-slate-300">{title}</p>
      <div className="mt-3 flex items-end justify-between gap-4">
        <p className="text-3xl font-semibold text-white">{value}</p>
        <span className="rounded-full border border-white/10 bg-white/5 px-3 py-1 text-xs text-slate-300">{helper}</span>
      </div>
    </div>
  );
}