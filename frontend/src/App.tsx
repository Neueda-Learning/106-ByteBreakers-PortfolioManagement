import { Navigate, RouterProvider, createBrowserRouter } from "react-router-dom";

import Layout from "@/components/layout/Layout";

import Dashboard from "@/pages/Dashboard";
import InvestmentOptions from "@/pages/InvestmentOptions";
import MyInvestments from "@/pages/MyInvestments";
import InvestmentDetails from "./pages/InvestmentDetails";
// import MarketSimulation from "@/pages/MarketSimulation";

import { ROUTES } from "@/constants/routes";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      {
        index: true,
        element: <Dashboard />,
      },
      {
        path: ROUTES.INVESTMENTOPTIONS,
        element: <InvestmentOptions />,
      },
      {
        path: ROUTES.MYINVESTMENTS,
        element: <MyInvestments />,
      },
      {
        path: ROUTES.INVESTMENTDETAILS,
        element: <InvestmentDetails />,
      },
      {
        path: "*",
        element: <Navigate to="/" replace />,
      },
    ],
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;