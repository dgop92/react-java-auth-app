import { Route, Routes } from "react-router-dom";
import Dashboard from "../features/dashboard/pages/Dashboard";
import { PrivateRoute } from "../features/account/providers/PrivateRoute";
import { SignUp } from "../features/account/pages/SignUp/SignUp";
import { LogIn } from "../features/account/pages/LogIn/LogIn";

export default function MainRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LogIn />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path="*" element={<p>nothing here</p>} />
      <Route element={<PrivateRoute />}>
        <Route path="/dashboard" element={<Dashboard />} />
      </Route>
    </Routes>
  );
}
