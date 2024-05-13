import { Route, Routes } from "react-router-dom";
import { PrivateRoute } from "../features/account/providers/PrivateRoute";
import { SignUp } from "../features/account/pages/SignUp/SignUp";
import { LogIn } from "../features/account/pages/LogIn/LogIn";
import { ProfilePage } from "../features/dashboard/pages/Profile/ProfilePage";
import { HomePage } from "../features/main/HomePage";

export default function MainRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LogIn />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path="*" element={<HomePage />} />
      <Route element={<PrivateRoute />}>
        <Route path="/dashboard" element={<ProfilePage />} />
      </Route>
    </Routes>
  );
}
