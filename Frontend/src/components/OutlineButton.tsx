import React from "react";

type Props = {
  children: React.ReactNode;
  type: "button" | "submit" | "reset" | undefined;
  color: string;
  className?: string;
  onClick?: () => void;
};

function OutlineButton({ children, type, color, className, onClick }: Props) {
  return (
    <button
      className={`py-2 px-5 my-4 border-2 border-${color}-400 text-${color} ${
        className ? className : ""
      }`}
      type={type}
      onClick={onClick ? onClick : () => {}}
    >
      {children}
    </button>
  );
}

export default OutlineButton;
