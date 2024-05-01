import { Route, Routes } from "react-router-dom";
import { Login } from "../features/account/pages/Login";
import { SignUp } from "../features/account/pages/SignUp";
import Dashboard from "../features/dashboard/pages/Dashboard";
import { PrivateRoute } from "../features/account/providers/PrivateRoute";

export default function MainRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path="*" element={<p>nothing here</p>} />
      <Route element={<PrivateRoute />}>
        <Route path="/dashboard" element={<Dashboard />} />
      </Route>
    </Routes>
  );
}
