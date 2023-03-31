import React from "react";

type Props = {};

function ErrorPage({}: Props) {
  return (
    <div className="min-h-screen flex justify-center items-center bg-[#0f172a] text-white p-5">
      <div className="border border-white px-3 md:px-7 py-9">
        <h1 className="text-3xl text-center">
          OOPS THE PAGE YOU ARE TRYING TO SEE DOES NOT EXIST
        </h1>
      </div>
    </div>
  );
}

export default ErrorPage;
