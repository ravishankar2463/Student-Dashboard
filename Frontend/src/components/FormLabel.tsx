import React from "react";

type Props = {
  htmlFor: string;
  name: string;
};

function FormLabel({ htmlFor, name }: Props) {
  return (
    <label className="mt-2" htmlFor={htmlFor}>
      {name}
    </label>
  );
}

export default FormLabel;
