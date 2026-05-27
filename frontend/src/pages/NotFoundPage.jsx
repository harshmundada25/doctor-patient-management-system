import { Link } from 'react-router-dom';

export default function NotFoundPage() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-hospital-radial px-6 text-center">
      <div className="glass-card max-w-lg p-10">
        <p className="text-xs uppercase tracking-[0.4em] text-cyan-300">404</p>
        <h1 className="mt-3 text-4xl font-semibold text-white">Page not found</h1>
        <p className="mt-4 text-slate-400">The requested route does not exist in the healthcare portal.</p>
        <Link className="btn-primary mt-6" to="/dashboard">Go to dashboard</Link>
      </div>
    </div>
  );
}