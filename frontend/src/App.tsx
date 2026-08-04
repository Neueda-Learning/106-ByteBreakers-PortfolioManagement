import { Navigate, RouterProvider, createBrowserRouter } from 'react-router-dom'
import { MainLayout } from './layouts/MainLayout'
import { DashboardPage } from './pages/DashboardPage'
import { InvestmentOptionsPage } from './pages/InvestmentOptionsPage'
import { InvestmentsPage } from './pages/InvestmentsPage'

const router = createBrowserRouter([
    {
        path: '/',
        element: <MainLayout />,
        children: [
            {
                index: true,
                element: <DashboardPage />,
            },
            {
                path: 'investments',
                element: <InvestmentsPage />,
            },
            {
                path: 'investment-options',
                element: <InvestmentOptionsPage />,
            },
            {
                path: '*',
                element: <Navigate to="/" replace />,
            },
        ],
    },
])

function App() {
    return <RouterProvider router={router} />
}

export default App
