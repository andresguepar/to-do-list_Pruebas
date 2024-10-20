package co.edu.unihumboldt.to_do_list.mapping.dtos;

import lombok.Data;

@Data
public class TaskStatsDto {
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long highPriorityTasks;
    private long mediumPriorityTasks;
    private long lowPriorityTasks;
    private double completionRate;
    private long taskCompletedOnTime;
    private long overDueTasks;
    private double averageRewardLevel;
}
