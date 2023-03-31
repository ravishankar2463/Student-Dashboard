import React from "react";
import { QueryClient, useQuery } from "react-query";
import { LoaderFunctionArgs, useLoaderData, useNavigate } from "react-router";
import { getStudent } from "../../api/studentDashboardApi";
import OutlineButton from "../../components/OutlineButton";
import { Student } from "../../models/Student";

type Props = {};

let backgroundColor = "bg-gradient-to-br from-slate-900 to-slate-700";

function StudentHome({}: Props) {
  const loaderData: Student = useLoaderData() as Student;
  const navigate = useNavigate();
  const { name } = loaderData;

  return (
    <div
      className={`min-h-[90vh] ${backgroundColor} flex justify-center items-center flex-col md:flex-row md:gap-10 text-white`}
    >
      <h1 className="text-3xl md:text-5xl">Welcome {name}</h1>
      <div className="flex md:flex-col gap-5">
        <OutlineButton
          onClick={() =>
            navigate(
              `/home/${loaderData?.id}/registeredCourses/${loaderData?.id}`
            )
          }
          className="md:text-2xl"
          color="white"
          type="button"
        >
          Show All Enlisted Courses
        </OutlineButton>
        <OutlineButton
          onClick={() =>
            navigate(
              `/home/${loaderData?.id}/registerNewCourse/${loaderData?.id}`
            )
          }
          className="md:text-2xl"
          color="white"
          type="button"
        >
          Enlist in a new Course
        </OutlineButton>
      </div>
    </div>
  );
}

export default StudentHome;

export const loader =
  (queryClient: QueryClient) =>
  ({ params }: LoaderFunctionArgs) => {
    const data = queryClient.fetchQuery(["student", params.studentId], () =>
      getStudent(Number(params.studentId))
    );

    return data;
  };
