import { FullPageBox } from "../../../../components/Layout/FullPageBox";
import { FormSide } from "./FormSide";
import { useThemeMediaQuery } from "../../../../styles/hooks";
import { ImgSide } from "../common/ImgSide";

export function LogIn(): JSX.Element {
  const isAboveMd = useThemeMediaQuery((theme) => theme.breakpoints.up("md"));

  return (
    <FullPageBox
      direction="row"
      sx={{ display: "grid", gridAutoFlow: "column", gridAutoColumns: "1fr" }}
    >
      <FormSide />
      {isAboveMd && <ImgSide />}
    </FullPageBox>
  );
}
