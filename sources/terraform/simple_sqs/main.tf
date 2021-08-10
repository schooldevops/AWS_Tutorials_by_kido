resource "aws_sqs_queue" "my_sqs_to_test" {
  name = var.SQS_NAME
  delay_seconds = var.DELAY_SEC
  max_message_size = var.MAX_MESSAGE_SIZE
  visibility_timeout_seconds = var.VISIBILITY_TIMEOUT_SECONDS
  message_retention_seconds = var.MESSAGE_RETENTION_SECONDS
  receive_wait_time_seconds = var.RECEIVE_WAIT_TIME_SECONDS
  fifo_queue = var.FIFO_QUEUE
  content_based_deduplication = var.CONTENT_BASED_DEDUPLICATION
}

resource "aws_sqs_queue_policy" "my_sqs_to_test_policy" {
  queue_url = aws_sqs_queue.my_sqs_to_test.id

  policy = <<POLICY
  {
    "Version": "2012-10-17",
    "Id": "sqspolicy",
    "Statement": [
      {
        "Sid": "First",
        "Effect": "Allow",
        "Principal": "*",
        "Action": "sqs:SendMessage",
        "Resource": "${aws_sqs_queue.my_sqs_to_test.arn}"
      }
    ]
  }
  POLICY
}