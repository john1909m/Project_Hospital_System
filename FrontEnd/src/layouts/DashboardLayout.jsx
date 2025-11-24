import { SidebarProvider } from "@/components/ui/sidebar";
import { AppSidebar } from "@/components/AppSidebar";
import { AppNavbar } from "@/components/AppNavbar";
import { Outlet, Navigate } from "react-router-dom";

export default function DashboardLayout({ role }) {
  const userRole = localStorage.getItem("userRole");

  if (!userRole) {
    return <Navigate to="/login" replace />;
  }

  if (userRole !== role) {
    return <Navigate to={`/${userRole}/dashboard`} replace />;
  }

  const getUserName = () => {
    switch (role) {
      case "admin":
        return "Admin User";
      case "doctor":
        return "Dr. Sarah Smith";
      case "patient":
        return "John Doe";
      default:
        return "User";
    }
  };

  const getRoleDisplay = () => {
    return role.charAt(0).toUpperCase() + role.slice(1);
  };

  return (
    <SidebarProvider>
      <div className="flex min-h-screen w-full transition-base">
        <AppSidebar role={role} />
        <div className="flex-1 flex flex-col transition-base">
          <AppNavbar userName={getUserName()} role={getRoleDisplay()} />
          <main className="flex-1 p-6 bg-background transition-base">
            <div className="max-w-7xl mx-auto">
              <Outlet />
            </div>
          </main>
        </div>
      </div>
    </SidebarProvider>
  );
}
