import { Box } from "@mui/material";
import { FullPageBox } from "../../../../components/Layout/FullPageBox";
import { FormSide } from "./FormSide";
import { useThemeMediaQuery } from "../../../../styles/hooks";

export function SignUp(): JSX.Element {
  const isAboveMd = useThemeMediaQuery((theme) => theme.breakpoints.up("md"));

  return (
    <FullPageBox
      direction="row"
      sx={{ display: "grid", gridAutoFlow: "column", gridAutoColumns: "1fr" }}
    >
      <FormSide />
      {isAboveMd && <Box sx={{ backgroundColor: "primary.dark" }}></Box>}
    </FullPageBox>
  );
}
