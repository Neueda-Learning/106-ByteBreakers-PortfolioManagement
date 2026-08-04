import { useState } from "react";
import { Outlet } from "react-router-dom";
import { Navbar } from "../navigation/Navbar";
import { Sidebar } from "../navigation/Sidebar";

export function MainLayout() {
  const [sidebarOpen, setSidebarOpen] = useState<boolean>(false);

  function sideBarChanger() {
    if (sidebarOpen) {
      setSidebarOpen(false);
    } else {
      setSidebarOpen(true);
    }
  }

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <Navbar onMenuClick={() => sideBarChanger()} />
      <Sidebar open={sidebarOpen} onClose={() => setSidebarOpen(false)} />

      <main className="pt-16 md:pl-72">
        <div className="mx-auto w-full max-w-[1600px] px-4 py-6 sm:px-6 lg:px-8">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
