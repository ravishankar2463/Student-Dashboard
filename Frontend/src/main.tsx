import React from "react";
import ReactDOM from "react-dom/client";
import { QueryClient, QueryClientProvider } from "react-query";
import { ReactQueryDevtools } from "react-query/devtools";
import { createRoutesFromElements, Route, RouterProvider } from "react-router";
import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import LoginForm, {
  action as studentLoginAction,
} from "./pages/onboarding/LoginForm";
import "./index.css";
import Login from "./pages/onboarding/Login";
import SignUp from "./pages/onboarding/SignUp";
import AddNewCourses, {
  loader as unregisteredCoursesLoader,
  action as enrollCourseAction,
} from "./pages/student/AddNewCourses";
import Home from "./pages/student/Home";
import RegisteredCourses, {
  loader as registeredCoursesLoader,
  action as unenrollCourseAction,
} from "./pages/student/RegisteredCourses";
import StudentHome, {
  loader as studentLoader,
} from "./pages/student/StudentHome";
import SignUpForm, {
  action as studentSignupAction,
} from "./pages/onboarding/SignUpForm";
import AuthContextProvider from "./store/AuthContext";
import ErrorPage from "./pages/error/ErrorPage";
import { Toaster } from "react-hot-toast";

const queryClient = new QueryClient({});

const router = createBrowserRouter(
  createRoutesFromElements(
    <Route path="/" element={<App />} errorElement={<ErrorPage />}>
      <Route path="login" element={<Login />}>
        <Route
          path="login-form"
          element={<LoginForm />}
          action={studentLoginAction(queryClient)}
        />
      </Route>
      <Route path="sign-up" element={<SignUp />}>
        <Route
          path="signup-form"
          element={<SignUpForm />}
          action={studentSignupAction(queryClient)}
        />
      </Route>
      <Route path="home/:studentId" element={<Home />}>
        <Route
          index
          element={<StudentHome />}
          loader={studentLoader(queryClient)}
        />
        <Route
          path="registeredCourses/:studentId"
          element={<RegisteredCourses />}
          loader={registeredCoursesLoader(queryClient)}
          action={unenrollCourseAction(queryClient)}
        />
        <Route
          path="registerNewCourse/:studentId"
          element={<AddNewCourses />}
          loader={unregisteredCoursesLoader(queryClient)}
          action={enrollCourseAction(queryClient)}
        />
      </Route>
    </Route>
  )
);

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <React.StrictMode>
    <AuthContextProvider>
      <QueryClientProvider client={queryClient}>
        <Toaster position="top-center" />
        <RouterProvider router={router} />
        <ReactQueryDevtools />
      </QueryClientProvider>
    </AuthContextProvider>
  </React.StrictMode>
);
