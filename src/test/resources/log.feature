Feature: RecordTime

    Scenario: Record a log
        Given I develop timelog for 1 hour
        When I record the activity
        Then the system should have the log record 