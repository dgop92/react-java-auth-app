export enum ErrorCode {
  INVALID_INPUT = "invalid-input",
  INVALID_ID = "invalid-id",
  INVALID_OPERATION = "invalid-operation",
  DUPLICATED_RECORD = "duplicated-record",
  ID_NOT_PROVIDED = "id-not-provided",
  NOT_FOUND = "not-found",
  UNAUTHORIZED = "unauthorized",
  FORBIDDEN = "forbidden",
  APPLICATION_INTEGRITY_ERROR = "application-integrity-error",
  UNKNOW_ERROR = "unknown-error",
}

export interface FieldError {
  path: string;
  message: string;
}

export class APIError extends Error {
  constructor(
    public message: string,
    public debugMessage: string,
    public statusCode: number,
    public errorCode: ErrorCode,
    public timestamp: string,
    public fieldErrors: FieldError[]
  ) {
    super(message);
  }
}
