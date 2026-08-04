export function Navbar({ onMenuClick }: { onMenuClick: () => void }) {
    return (
        <header className="fixed inset-x-0 top-0 z-40 border-b border-slate-200 bg-white/95 backdrop-blur">
            <div className="mx-auto flex h-16 w-full max-w-[1600px] items-center justify-between px-4 sm:px-6 lg:px-8">
                <div className="flex items-center gap-3">
                    <button
                        type="button"
                        onClick={onMenuClick}
                        className="inline-flex h-10 w-10 items-center justify-center rounded-lg border border-slate-300 text-slate-700 transition hover:bg-slate-100 md:hidden"
                        aria-label="Open sidebar"
                    >
                        <span className="text-xl leading-none">☰</span>
                    </button>
                    <div>
                        <p className="text-xs font-semibold uppercase tracking-[0.24em] text-teal-600">Portfolio Manager</p>
                    </div>
                </div>
            </div>
        </header>
    )
}
