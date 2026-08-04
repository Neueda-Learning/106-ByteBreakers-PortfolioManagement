import { Navigate, RouterProvider, createBrowserRouter } from "react-router-dom";

import Layout from "@/components/layout/Layout";

import Dashboard from "@/pages/Dashboard";
import InvestmentOptions from "@/pages/InvestmentOptions";
// import InvestmentExplorer from "@/pages/InvestmentExplorer";
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
      // {
      //   path: ROUTES.EXPLORER,
      //   element: <InvestmentExplorer />,
      // },
      // {
      //   path: ROUTES.SIMULATION,
      //   element: <MarketSimulation />,
      // },
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