export function PageHeader({ title, subtitle }: { title: string; subtitle: string }) {
    return (
        <header className="mb-6">
            <h1 className="text-2xl font-bold text-slate-900 sm:text-3xl">{title}</h1>
            <p className="mt-1 text-sm text-slate-600 sm:text-base">{subtitle}</p>
        </header>
    )
}
