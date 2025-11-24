import SiteLayout from "../layout/SiteLayout";
import { createBrowserRouter } from "react-router-dom";
import Login from "../pages/Login";
import Signup from "../pages/signup";
export const routes = createBrowserRouter([
    {
        path: "/",
        Component:SiteLayout,
        children: [
            {path: "/", Component: Login},
            {path: "/Signup", Component: Signup}

        ]
    }
])