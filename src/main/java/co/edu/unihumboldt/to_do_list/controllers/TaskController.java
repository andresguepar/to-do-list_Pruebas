package co.edu.unihumboldt.to_do_list.controllers;

import co.edu.unihumboldt.to_do_list.domain.enums.Priority;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskDto;
import co.edu.unihumboldt.to_do_list.mapping.dtos.TaskStatsDto;
import co.edu.unihumboldt.to_do_list.services.impl.TaskServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/task")
public class TaskController {
    public final TaskServiceImpl service;

    public TaskController(TaskServiceImpl service) {
        this.service = service;
    }
    // 1.Listar Tareas Creadas
    @GetMapping("/list")
    public ModelAndView listTask(){
        ModelAndView modelAndView = new ModelAndView("taskList");
        modelAndView.addObject("task", service.list());
        return modelAndView;
    }
    // 2.Crear Tareas
    @GetMapping("/create")
    public ModelAndView showCreateForm(){
        return new ModelAndView("formTask").addObject("task", new TaskDto());
    }
    @PostMapping("/new")
    public String createNewtask(@ModelAttribute TaskDto taskDto,
                                RedirectAttributes redirectAttributes){
        try {
            service.save(taskDto);
            redirectAttributes.addFlashAttribute("success", "Task created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating the task.");
        }
        return "redirect:/task/create";
    }

    // 2.Actualizar Tareas
    @GetMapping("/update-form/{id}")
    public ModelAndView showUpdateForm(@PathVariable("id") int id,
                                       RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("formTaskUpdate");

        Optional<TaskDto> taskDto = Optional.ofNullable(service.byId(id));

        if (taskDto.isPresent()) {
            modelAndView.addObject("task", taskDto.get());
        } else {
            redirectAttributes.addFlashAttribute("error", "Task not found.");
            modelAndView.setViewName("redirect:/task/list");
        }
        return modelAndView;
    }
    @PostMapping("/update")
    public String updateTask(@ModelAttribute TaskDto taskDto, RedirectAttributes redirectAttributes) {

        Optional<TaskDto> existingTask = Optional.ofNullable(service.byId(taskDto.getId()));

        if (existingTask.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Task not found.");
            return "redirect:/task/list";
        }

        service.save(taskDto);
        redirectAttributes.addFlashAttribute("success", "Task updated successfully.");

        return "redirect:/task/list";
    }
    // 3.Eliminar Tareas
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id")Integer id){
        service.delete(id);
        return "redirect:/task/list";
    }
    // 4. Asignar prioridad según la fecha límite
    @GetMapping("/assign-priority")
    public Priority assignPriority(@RequestParam("limitDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate limitDate) {
        return service.assignPriority(limitDate);
    }

    // 5. Activar modo enfoque para una tarea específica
    @PostMapping("/focus-mode/{id}")
    public String enterFocusMode(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try{
            service.focusMode(id);
            redirectAttributes.addFlashAttribute("succes","Focus Mode Activaded for task" + id);

        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Error activating focus mode: " + e.getMessage());
        }

        return "redirect:/task/list";
    }

    // 6. Salir del modo enfoque
    @PostMapping("/exit-focus-mode")
    public String exitFocusMode(RedirectAttributes redirectAttributes) {
        try{
            service.exitFocusMode();
            redirectAttributes.addFlashAttribute("succes","Focus Mode deactivaded for task");

        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Error deactivating focus mode: " + e.getMessage());
        }

        return "redirect:/task/list";
    }

    // 7. Obtener tareas visibles (según si está en modo enfoque o no)
    @GetMapping("/visible")
    public List<TaskDto> getVisibleTasks() {
        return service.getVisibleTasks();
    }

    // 8. Obtener estadísticas de tareas
    @GetMapping("/stats")
    public TaskStatsDto getTaskStats() {
        return service.getStats();
    }

    // 9. Obtener tareas ordenadas por prioridad
    @GetMapping("/sorted-by-priority")
    public ModelAndView getTasksSortedByPriority() {
        try {
            List<TaskDto> tasks = service.getTasksSortedByPriority();
            ModelAndView modelAndView = new ModelAndView("taskList");
            modelAndView.addObject("task",tasks);
            modelAndView.addObject("stats",service.getStats());
            return modelAndView;
        }catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("redirect:/task/list");
            modelAndView.addObject("error", "Error sorting tasks by priority");
            return modelAndView;
        }
    }

    // 10. Obtener tareas ordenadas por fecha de vencimiento
    @GetMapping("/sorted-by-due-date")
    public ModelAndView getTasksSortedByDueDate() {
        try {
            List<TaskDto> tasks = service.getTasksSortedByDueDate();
            ModelAndView modelAndView = new ModelAndView("taskList");
            modelAndView.addObject("task", tasks);
            modelAndView.addObject("stats", service.getStats());
            return modelAndView;
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("redirect:/task/list");
            modelAndView.addObject("error", "Error sorting tasks by due date");
            return modelAndView;
        }
    }

    // 11. Obtener tareas ordenadas por estado de finalización
    @GetMapping("/sorted-by-completion-status")
    public ModelAndView getTasksSortedByCompletionStatus() {
        try {
            List<TaskDto> sortedTasks = service.getTasksSortedByCompletionStatus();
            ModelAndView modelAndView = new ModelAndView("taskList");
            modelAndView.addObject("task", sortedTasks);
            modelAndView.addObject("stats", service.getStats()); // Añadimos las estadísticas para el modal de stats
            return modelAndView;
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView("redirect:/task/list");
            modelAndView.addObject("error", "Error sorting tasks by completion status");
            return modelAndView;
        }
    }
    @PostMapping("/toggle-complete/{id}")
    public String toggleComplete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            TaskDto task = service.byId(id);
            task.setCompleted(!task.isCompleted());
            if (task.isCompleted()) {
                task.setCompletedDate(LocalDate.now());
            } else {
                task.setCompletedDate(null);
            }
            service.save(task);
            redirectAttributes.addFlashAttribute("success", "Task status updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating task status.");
        }
        return "redirect:/task/list";
    }
}
