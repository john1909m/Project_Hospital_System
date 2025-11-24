import { Navigate, Outlet } from "react-router-dom";

export default function ProtectedRoute({ allowedRole }) {
  const token = localStorage.getItem("token");
  const userRole = localStorage.getItem("userRole")?.toLowerCase();

    if (!token || !userRole) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRole && allowedRole !== userRole) {
    return <Navigate to={`/${userRole}/dashboard`} replace />;
  }

  return <Outlet />;

}