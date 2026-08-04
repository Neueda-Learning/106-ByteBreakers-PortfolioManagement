import { PageHeader } from '../components/common/PageHeader'
import { InvestmentsTable } from '../components/investments/InvestmentsTable'
import { useInvestments } from '../hooks/useInvestments'

export function InvestmentsPage() {
    const { investments, loading } = useInvestments()

    return (
        <section>
            <PageHeader
                title="Investment"
                subtitle="Table"
            />

            {loading ? (
                <div className="rounded-xl border border-slate-200 bg-white p-6 text-sm text-slate-600 shadow-sm">
                    Loading investments...
                </div>
            ) : null}

            {!loading && investments.length === 0 ? (
                <div className="rounded-xl border border-slate-200 bg-white p-6 text-sm text-slate-600 shadow-sm">
                    No investment records found.
                </div>
            ) : null}

            {!loading && investments.length > 0 ? <InvestmentsTable investments={investments} /> : null}
        </section>
    )
}
