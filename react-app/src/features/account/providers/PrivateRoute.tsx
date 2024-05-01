import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./hooks";

export interface PrivateRouteProps {
  children?: JSX.Element;
}

// reference: https://www.robinwieruch.de/react-router-private-routes/

export function PrivateRoute({ children }: PrivateRouteProps) {
  const { firebaseUser } = useAuth();

  if (!firebaseUser) {
    // not logged in so redirect to login page with the return url
    return <Navigate to="/login" replace />;
  }

  // authorized so return child components
  return children || <Outlet />;
}
