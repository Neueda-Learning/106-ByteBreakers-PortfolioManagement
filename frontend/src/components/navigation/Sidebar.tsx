import { NavLink } from "react-router-dom";

export function Sidebar({ open, onClose }: any) {
    return (
        <>
            {open && (
                <button
                    type="button"
                    aria-label="Close sidebar"
                    className="fixed inset-0 z-30 bg-slate-900/40 md:hidden"
                    onClick={onClose}
                />
            )}

            <aside
                className={`fixed left-0 top-16 z-40 h-[calc(100dvh-4rem)] w-72 border-r border-slate-200 bg-white px-4 py-5 transition-transform md:translate-x-0 ${open ? "translate-x-0" : "-translate-x-full"
                    }`}
            >
                <nav className="space-y-1">
                    <NavLink
                        to="/"
                        end
                        onClick={onClose}
                        className={({ isActive }) =>
                            `block rounded-lg px-3 py-2 text-sm font-medium transition ${isActive
                                ? "bg-teal-100 text-teal-900"
                                : "text-slate-700 hover:bg-slate-100 hover:text-slate-900"
                            }`
                        }
                    >
                        Dashboard
                    </NavLink>

                    <NavLink
                        to="/investments"
                        onClick={onClose}
                        className={({ isActive }) =>
                            `block rounded-lg px-3 py-2 text-sm font-medium transition ${isActive
                                ? "bg-teal-100 text-teal-900"
                                : "text-slate-700 hover:bg-slate-100 hover:text-slate-900"
                            }`
                        }
                    >
                        My Investments
                    </NavLink>

                    <NavLink
                        to="/investment-options"
                        onClick={onClose}
                        className={({ isActive }) =>
                            `block rounded-lg px-3 py-2 text-sm font-medium transition ${isActive
                                ? "bg-teal-100 text-teal-900"
                                : "text-slate-700 hover:bg-slate-100 hover:text-slate-900"
                            }`
                        }
                    >
                        Investment Options
                    </NavLink>
                </nav>
            </aside>
        </>
    );
}