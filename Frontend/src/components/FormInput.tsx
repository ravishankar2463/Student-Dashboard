import React, { forwardRef } from "react";

type Props = {
  id: string;
  type: string;
  placeholder?: string;
};

const FormInput = forwardRef<HTMLInputElement, Props>(function formInput(
  { id, type, placeholder }: Props,
  ref
) {
  return (
    <input
      className="block border border-black rounded-md mt-1 p-2"
      id={id}
      type={type}
      placeholder={placeholder}
      name={id}
      ref={ref}
    />
  );
});

export default FormInput;
