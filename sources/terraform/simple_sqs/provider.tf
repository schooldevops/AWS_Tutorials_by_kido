provider "aws" {
  region = var.AWS_REGION
  profile = var.PROFILE
  shared_credentials_file = var.CREDENTIAL_FILES
}