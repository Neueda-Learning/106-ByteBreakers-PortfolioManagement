export function InvestmentsTable({ investments }: any) {
    return (
        <div className="overflow-x-auto rounded-xl border border-slate-200 bg-white shadow-sm">
            <table className="min-w-full">
                <thead className="bg-slate-100">
                    <tr>
                        <th className="px-4 py-3 text-left">Id</th>
                        <th className="px-4 py-3 text-left">Investment Option Id</th>
                        <th className="px-4 py-3 text-left">Quantity</th>
                        <th className="px-4 py-3 text-left">Total Invested</th>
                        <th className="px-4 py-3 text-left">Purchase Date</th>
                        <th className="px-4 py-3 text-left">Action</th>
                    </tr>
                </thead>
                <tbody>
                    {investments.map((investment: any) => {
                        return (
                            <tr key={investment.id} className="border-t">
                                <td className="px-4 py-3">{investment.id}</td>
                                <td className="px-4 py-3">{investment.investmentOptionId}</td>
                                <td className="px-4 py-3">{investment.quantity}</td>
                                <td className="px-4 py-3">{investment.totalInvested}</td>
                                <td className="px-4 py-3">{investment.purchaseDate}</td>
                                <td className="px-4 py-3">
                                    <button className="rounded bg-blue-600 px-3 py-1 text-white">View More</button>
                                </td>
                            </tr>
                        )
                    })}
                </tbody>
            </table>
        </div>
    )
}