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
      {isAboveMd && (
        <Box display="flex" height="100vh">
          <Box
            component="img"
            src="/plants.jpg"
            alt="green leaves of a plant"
            sx={{
              objectFit: "cover",
              width: "100%",
              height: "100%",
            }}
          />
        </Box>
      )}
    </FullPageBox>
  );
}
