output "sqs_queue_id" {
  description = "SQS URL을 반환한다."
  value = aws_sqs_queue.my_sqs_to_test.id
}

output "sqs_queue_name" {
  description = "SQS Queue 의 이름"
  value = aws_sqs_queue.my_sqs_to_test.name
}