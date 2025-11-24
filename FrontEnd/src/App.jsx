import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Login from "./pages/Login";
import Signup from "./pages/signup";

import ProtectedRoute from "./routes/ProtectedRoutes";
import DashboardLayout from "./layouts/DashboardLayout";

import AdminDashboard from "./pages/admin/Dashboard";


import DoctorDashboard from "./pages/doctor/Dashboard";
import PatientDashboard from "./pages/patient/Dashboard";

import NotFound from "./pages/NotFound";

const queryClient = new QueryClient();

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <TooltipProvider>
        <Toaster />
        <Sonner />
        <BrowserRouter>
          <Routes>

            {/* Default redirect */}
            <Route path="/" element={<Navigate to="/login" replace />} />

            {/* Public routes */}
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<Signup />} />

            {/* Protected Admin */}
            <Route element={<ProtectedRoute allowedRole="admin" />}>
              <Route path="/admin" element={<DashboardLayout role="admin" />}>
                <Route path="dashboard" element={<AdminDashboard />} />
                <Route index element={<Navigate to="/admin/dashboard" replace />} />
              </Route>
            </Route>

            {/* Protected Doctor */}
            <Route element={<ProtectedRoute allowedRole="doctor" />}>
              <Route path="/doctor" element={<DashboardLayout role="doctor" />}>
                <Route path="dashboard" element={<DoctorDashboard />} />
                <Route index element={<Navigate to="/doctor/dashboard" replace />} />
              </Route>
            </Route>

            {/* Protected Patient */}
            <Route element={<ProtectedRoute allowedRole="patient" />}>
              <Route path="/patient" element={<DashboardLayout role="patient" />}>
                <Route path="dashboard" element={<PatientDashboard />} />
                <Route index element={<Navigate to="/patient/dashboard" replace />} />
              </Route>
            </Route>

            <Route path="*" element={<NotFound />} />

          </Routes>
        </BrowserRouter>
      </TooltipProvider>
    </QueryClientProvider>
  );
}
