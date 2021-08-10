variable "AWS_REGION" {
  default = "ap-northeast-2"
}

variable "SQS_NAME" {
  description = "This is sqs name to be created"
  default = "my-todo2"
}

variable "PROFILE" {
  description = "This is an aws profile for shared credentials file"
  default = "terraform_user"
}

variable "CREDENTIAL_FILES" {
  description = "This is a path of credentials file"
  default = "/Users/kido/.aws/credentials"
}

variable "DELAY_SEC" {
  description = "대기열의 모슨 메시지 배달이 지연되는 시간 (초) 기본은 0, 0 ~ 900 까지 가능"
  type = number
  default = 0
}

variable "MAX_MESSAGE_SIZE" {
  description = "메시지가 리젝되기 전에 가질 수 있는 바이트 수, 1024 ~ 262144 바이트까지 가능, 기본값 = 262144"
  type = number
  default = 262144
}

variable "MESSAGE_RETENTION_SECONDS" {
  description = "SQS가 메시지를 보관하는 시간, 60초 ~ 1209600 (14일), 기본값 345600 (4일)"
  type = number
  default = 345600
}

variable "RECEIVE_WAIT_TIME_SECONDS" {
  description = "ReceiveMessage 호출이 반환되기 전에 메시지가 도착하기를 기다리는 시간, 0 ~ 20초 사이"
  type = number
  default = 0
}

variable "VISIBILITY_TIMEOUT_SECONDS" {
  description = "큐에서 가시성 타임아웃, 0 에서 43200 (12 hours)"
  type        = number
  default     = 30
}

variable "FIFO_QUEUE" {
  description = "FIFO Queue 를 만들지 여부"
  type = bool
  default = false
}

variable "CONTENT_BASED_DEDUPLICATION" {
  description = "FIFO 큐에서 컨텐츠 기반의 중복 제거를 활성화 할지 여부"
  type        = bool
  default     = false
}