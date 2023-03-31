import React from "react";
import { NavLink } from "react-router-dom";
import LoginForm from "./LoginForm";

type Props = {};

function Login({}: Props) {
  return (
    <div className="min-h-screen p-5 flex justify-center items-center bg-[#0f172a] text-white">
      <div className="flex flex-col md:flex-row">
        <div className="py-20 px-10 border border-white">
          <h1 className="text-5xl md:text-8xl pb-2">Hi Student</h1>
          <h2 className="text-2xl md:text-4xl">
            Please enter your credentials to Login
          </h2>
          <h1 className="pt-4">
            If you are new{" "}
            <NavLink className="underline underline-offset-4" to="/sign-up">
              Click Here
            </NavLink>{" "}
            to sign-up.
          </h1>
        </div>
        <div className="bg-white text-black flex justify-center items-center">
          <LoginForm />
        </div>
      </div>
    </div>
  );
}

export default Login;
