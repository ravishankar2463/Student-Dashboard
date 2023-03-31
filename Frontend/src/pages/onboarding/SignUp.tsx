import React from "react";
import SignUpForm from "./SignUpForm";

type Props = {};

function SignUp({}: Props) {
  return (
    <div className="min-h-screen p-5 flex flex-col justify-center items-center bg-[#0f172a] text-white">
      <h1 className="text-5xl pb-5">Sign Up</h1>
      <div className="bg-white text-black flex justify-center items-center rounded-sm p-5">
        <SignUpForm />
      </div>
    </div>
  );
}

export default SignUp;
