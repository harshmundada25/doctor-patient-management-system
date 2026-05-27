import { Link, NavLink, Outlet } from 'react-router-dom';
import { Bell, Calendar, CircleUserRound, ClipboardList, LayoutDashboard, MoonStar, Stethoscope, Users, LogOut, SunMedium, ShieldCheck } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { useTheme } from '../context/ThemeContext';

const roleMenus = {
  ADMIN: [
    { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/doctors', label: 'Doctors', icon: Stethoscope },
    { to: '/patients', label: 'Patients', icon: Users },
    { to: '/appointments', label: 'Appointments', icon: Calendar },
    { to: '/prescriptions', label: 'Prescriptions', icon: ClipboardList },
    { to: '/profile', label: 'Profile', icon: CircleUserRound },
  ],
  DOCTOR: [
    { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/patients', label: 'My Patients', icon: Users },
    { to: '/appointments', label: 'Appointments', icon: Calendar },
    { to: '/prescriptions', label: 'Prescriptions', icon: ClipboardList },
    { to: '/profile', label: 'Profile', icon: CircleUserRound },
  ],
  PATIENT: [
    { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/appointments', label: 'Appointments', icon: Calendar },
    { to: '/prescriptions', label: 'Prescriptions', icon: ClipboardList },
    { to: '/profile', label: 'Profile', icon: CircleUserRound },
  ],
};

export default function Layout() {
  const { user, logout } = useAuth();
  const { darkMode, toggleTheme } = useTheme();
  const menus = roleMenus[user?.role] || [];

  return (
    <div className="min-h-screen bg-hospital-radial text-slate-100">
      <div className="mx-auto flex min-h-screen max-w-[1600px]">
        <aside className="hidden w-72 flex-col border-r border-white/10 bg-slate-950/80 p-6 backdrop-blur xl:flex">
          <Link to="/dashboard" className="flex items-center gap-3 rounded-3xl border border-cyan-400/20 bg-cyan-400/10 px-4 py-4">
            <div className="rounded-2xl bg-cyan-400 p-3 text-slate-950">
              <ShieldCheck size={22} />
            </div>
            <div>
              <p className="text-sm font-semibold uppercase tracking-[0.3em] text-cyan-200">Apex Care</p>
              <p className="text-xs text-slate-300">Hospital Management Suite</p>
            </div>
          </Link>

          <nav className="mt-8 space-y-2">
            {menus.map(({ to, label, icon: Icon }) => (
              <NavLink
                key={to}
                to={to}
                className={({ isActive }) =>
                  `flex items-center gap-3 rounded-2xl px-4 py-3 text-sm font-medium transition ${isActive ? 'bg-cyan-400 text-slate-950' : 'text-slate-300 hover:bg-white/5 hover:text-white'}`
                }
              >
                <Icon size={18} />
                {label}
              </NavLink>
            ))}
          </nav>

          <div className="mt-auto space-y-4 rounded-3xl border border-white/10 bg-white/5 p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-2xl bg-emerald-400/15 p-3 text-emerald-300">
                <Bell size={18} />
              </div>
              <div>
                <p className="text-sm font-semibold">Session Active</p>
                <p className="text-xs text-slate-400">JWT secured access</p>
              </div>
            </div>
            <button className="btn-secondary w-full justify-center" onClick={toggleTheme} type="button">
              {darkMode ? <SunMedium size={16} className="mr-2" /> : <MoonStar size={16} className="mr-2" />}
              {darkMode ? 'Light mode' : 'Dark mode'}
            </button>
            <button className="btn-primary w-full justify-center bg-rose-500 text-white hover:bg-rose-400" onClick={logout} type="button">
              <LogOut size={16} className="mr-2" /> Logout
            </button>
          </div>
        </aside>

        <main className="flex-1">
          <header className="sticky top-0 z-30 border-b border-white/10 bg-slate-950/70 px-4 py-4 backdrop-blur xl:px-8">
            <div className="glass-card flex items-center justify-between px-4 py-3 xl:px-6">
              <div>
                <p className="text-xs uppercase tracking-[0.35em] text-cyan-300">Enterprise Healthcare Platform</p>
                <h1 className="text-lg font-semibold text-white xl:text-2xl">Doctor & Patient Management System</h1>
              </div>
              <div className="flex items-center gap-3">
                <button className="btn-secondary xl:hidden" onClick={toggleTheme} type="button">
                  {darkMode ? <SunMedium size={16} /> : <MoonStar size={16} />}
                </button>
                <div className="hidden text-right sm:block">
                  <p className="text-sm font-semibold text-white">{user?.fullName}</p>
                  <p className="text-xs text-slate-400">{user?.role}</p>
                </div>
                <div className="rounded-2xl border border-white/10 bg-white/5 p-3 text-cyan-300">
                  <CircleUserRound size={20} />
                </div>
              </div>
            </div>
          </header>

          <div className="px-4 py-6 xl:px-8 xl:py-8">
            <Outlet />
          </div>
        </main>
      </div>

      <div className="fixed inset-x-0 bottom-0 z-40 border-t border-white/10 bg-slate-950/90 px-2 py-2 backdrop-blur xl:hidden">
        <div className="flex items-center justify-between gap-1 overflow-x-auto">
          {menus.map(({ to, label, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              className={({ isActive }) =>
                `flex min-w-[72px] flex-1 flex-col items-center gap-1 rounded-2xl px-3 py-2 text-[11px] transition ${isActive ? 'bg-cyan-400 text-slate-950' : 'text-slate-300 hover:bg-white/5'}`
              }
            >
              <Icon size={16} />
              {label}
            </NavLink>
          ))}
          <button className="flex min-w-[72px] flex-1 flex-col items-center gap-1 rounded-2xl px-3 py-2 text-[11px] text-slate-300 hover:bg-white/5" onClick={toggleTheme} type="button">
            {darkMode ? <SunMedium size={16} /> : <MoonStar size={16} />}
            Theme
          </button>
          <button className="flex min-w-[72px] flex-1 flex-col items-center gap-1 rounded-2xl px-3 py-2 text-[11px] text-rose-300 hover:bg-white/5" onClick={logout} type="button">
            <LogOut size={16} />
            Logout
          </button>
        </div>
      </div>
    </div>
  );
}