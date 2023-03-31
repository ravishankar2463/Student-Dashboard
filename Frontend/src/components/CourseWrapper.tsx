import React, { ReactNode } from "react";

type Props = {
  children?: ReactNode;
  className?: string;
};

function CourseWrapper({ children, className }: Props) {
  return (
    <div
      className={`flex flex-col mt-2 bg-white text-black rounded-sm ${className}`}
    >
      {children}
    </div>
  );
}

export default CourseWrapper;
