# Task Completion Commands

Upon completion of a task, the following steps and commands should be performed:

1.  **Update Task Checklist**: Immediately after completing a task and verifying its correctness (including tests), mark the corresponding item in the task checklist (`<task_file>.md`) as completed. The format is `- [x] Task description (Đã hoàn thành)`.

2.  **Commit Changes**: Once the task is completed and the checklist is updated, the changes are ready for commit. The commit message should follow the Conventional Commits format:
    -   `feat: Add new feature`
    -   `fix: Fix bug`
    -   `docs: Update documentation`
    -   `refactor: Improve code structure`
    -   `test: Add unit test`
    -   `chore: Update build script`

3.  **Run Tests**: Ensure all unit and integration tests pass before committing. For Maven projects, this can be done via `mvn test`.