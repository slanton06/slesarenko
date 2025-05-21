package com.taskmanager.taskmanager.ui;

import com.taskmanager.taskmanager.model.Task;
import com.taskmanager.taskmanager.service.TaskService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Route("")
@CssImport("./styles/styles.css")
public class TaskView extends VerticalLayout {
    private final TaskService taskService;
    private Grid<Task> taskGrid = new Grid<>(Task.class);
    private Button openTaskDialogButton = new Button("Create a task", event -> openTaskDialog());

    public TaskView(TaskService taskService) {
        this.taskService = taskService;

        // Удаляем все колонки и добавляем их заново
        taskGrid.removeAllColumns();

        // Столбец "Name" с индикаторами:
        // Если статус == "New" – слева от имени появляется синяя точка,
        // если статус == "In process" – слева появляется желтая точка.
        taskGrid.addColumn(new ComponentRenderer<>(task -> {
            HorizontalLayout layout = new HorizontalLayout();
            layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

            // Добавляем индикатор, в зависимости от статуса
            if ("New".equals(task.getStatus())) {
                Span newIndicator = new Span("•");
                newIndicator.getElement().getStyle()
                        .set("color", "blue")
                        .set("font-size", "20px")
                        .set("padding-right", "8px");
                layout.add(newIndicator);
            } else if ("In process".equals(task.getStatus())) {
                Span inProcessIndicator = new Span("•");
                inProcessIndicator.getElement().getStyle()
                        .set("color", "orange")
                        .set("font-size", "20px")
                        .set("padding-right", "8px");
                layout.add(inProcessIndicator);
            } else if ("Done".equals(task.getStatus())) {
                Span doneIndicator = new Span("•");
                doneIndicator.getElement().getStyle()
                        .set("color", "grey")
                        .set("font-size", "20px")
                        .set("padding-right", "8px");
                layout.add(doneIndicator);
            }
            Span nameSpan = new Span(task.getName());
            layout.add(nameSpan);
            return layout;
        })).setHeader("Name").setKey("name");

        // Колонка для описания
        taskGrid.addColumn(Task::getDescription)
                .setHeader("Description")
                .setKey("description");

        // Колонка для статуса с выпадающим списком через ComponentRenderer
        taskGrid.addColumn(new ComponentRenderer<>(task -> {
            ComboBox<String> statusComboBox = new ComboBox<>();
            statusComboBox.setItems("New", "In process", "Done");
            statusComboBox.setValue(task.getStatus());
            statusComboBox.addValueChangeListener(event -> {
                String newStatus = event.getValue();
                task.setStatus(newStatus);
                taskService.updateTask(task);
                // При смене статуса перерисовываем строку
                taskGrid.getDataProvider().refreshItem(task);
                refreshGridItems();
            });
            return statusComboBox;
        })).setHeader("Status").setKey("status");

        // Форматирование даты для удобного отображения
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

        // Колонка для времени создания задачи
        taskGrid.addColumn(task ->
                task.getCreationTime() != null ? task.getCreationTime().format(formatter) : ""
        ).setHeader("Created At").setKey("creationTime");

        // Колонка для дедлайна с подсветкой текста и красной точкой, если дедлайн близкий
        taskGrid.addColumn(new ComponentRenderer<>(task -> {
            LocalDateTime deadline = task.getDeadline();
            LocalDateTime now = LocalDateTime.now();
            long hoursUntilDeadline = deadline != null ? ChronoUnit.HOURS.between(now, deadline) : Long.MAX_VALUE;

            Span deadlineSpan = new Span(deadline != null ? deadline.format(formatter) : "");
            if (hoursUntilDeadline < 24 && hoursUntilDeadline >= 0) {
                deadlineSpan.getElement().getStyle().set("color", "red");
            }
            HorizontalLayout deadlineContainer = new HorizontalLayout(deadlineSpan);
            deadlineContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            if (hoursUntilDeadline < 24 && hoursUntilDeadline >= 0) {
                Span indicator = new Span("•");
                indicator.getElement().getStyle()
                        .set("color", "red")
                        .set("font-size", "20px")
                        .set("margin-left", "8px");
                deadlineContainer.add(indicator);
            }
            return deadlineContainer;
        })).setHeader("Deadline").setKey("deadline");

        // Колонка с кнопкой редактирования
        taskGrid.addComponentColumn(task -> {
            Button editButton = new Button(new Icon("cog"), event -> openEditDialog(task));
            return editButton;
        });

        // Если задача со статусом "Done", добавляем класс, чтобы сделать строку тусклее
        taskGrid.setClassNameGenerator(task -> "Done".equals(task.getStatus()) ? "done-task" : "");

        // Перед установкой элементов, сортируем задачи: "Done" будут в конце
        refreshGridItems();

        add(openTaskDialogButton, taskGrid);
    }

    // Этот метод получает список задач, сортирует их так,
    // чтобы задачи со статусом "Done" были в конце, и затем устанавливает в Grid.
    private void refreshGridItems() {
        List<Task> tasks = new ArrayList<>(taskService.getAllTasks());
        tasks.sort(
                Comparator.<Task, Integer>comparing(task -> {
                            String status = task.getStatus() != null ? task.getStatus().trim().toLowerCase() : "";
                            return "done".equals(status) ? 1 : 0;
                        })
                        .thenComparing(Task::getName)
                        .thenComparing(Task::getCreationTime, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Task::getDeadline, Comparator.nullsLast(Comparator.naturalOrder()))
        );
        taskGrid.setItems(tasks);
    }

    // Методы для работы с cookies (с уникальным ключом, включающим taskId)
    private void setCookie(String taskId, String field, String value) {
        UI.getCurrent().getPage().executeJs("document.cookie = '"
                + field + "_" + taskId + "=" + value + "; path=/';");
    }

    private void getCookie(String taskId, String field, Consumer<String> callback) {
        UI.getCurrent().getPage().executeJs(
                "var cookies = document.cookie.split('; ');" +
                        "var result = ''; cookies.forEach(function(row) {" +
                        "if (row.startsWith('" + field + "_" + taskId + "=')) result = row.split('=')[1]; });" +
                        "return result;"
        ).then(jsonValue -> callback.accept(jsonValue.asString()));
    }

    private void openTaskDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setHeight("300px");

        TextField nameField = new TextField("Name");
        TextField descriptionField = new TextField("Description");

        // Компонент для выбора статуса
        ComboBox<String> statusComboBox = new ComboBox<>("Status");
        statusComboBox.setItems("New", "In process", "Done");
        statusComboBox.setValue("New");

        // Компонент для дедлайна; по умолчанию выставляем текущую дату + 1 день
        DateTimePicker deadlinePicker = new DateTimePicker("Deadline");
        deadlinePicker.setValue(LocalDateTime.now().plusDays(1));

        Button saveButton = new Button("Save", event -> {
            Task task = new Task();
            task.setName(nameField.getValue());
            task.setDescription(descriptionField.getValue());
            task.setStatus(statusComboBox.getValue());
            task.setCreationTime(LocalDateTime.now());
            task.setDeadline(deadlinePicker.getValue());
            taskService.addTask(task);
            refreshGridItems();
            dialog.close();
        });

        dialog.add(nameField, descriptionField, statusComboBox, deadlinePicker, saveButton);
        dialog.open();
    }

    private void openEditDialog(Task task) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setHeight("300px");

        // Сохраняем значения задачи в cookies
        setCookie(String.valueOf(task.getId()), "task_name", task.getName());
        setCookie(String.valueOf(task.getId()), "task_description", task.getDescription());
        setCookie(String.valueOf(task.getId()), "task_deadline", task.getDeadline().toString());

        TextField nameField = new TextField("Name", task.getName());
        TextField descriptionField = new TextField("Description", task.getDescription());
        DateTimePicker deadlinePicker = new DateTimePicker("Deadline", task.getDeadline());

        getCookie(String.valueOf(task.getId()), "task_name", savedName -> {
            if (nameField.getValue().isEmpty()) nameField.setValue(savedName);
        });
        getCookie(String.valueOf(task.getId()), "task_description", savedDescription -> {
            if (descriptionField.getValue().isEmpty()) descriptionField.setValue(savedDescription);
        });
        getCookie(String.valueOf(task.getId()), "task_deadline", savedDeadline -> {
            if (deadlinePicker.getValue() == null && savedDeadline != null && !savedDeadline.isEmpty()) {
                deadlinePicker.setValue(LocalDateTime.parse(savedDeadline));
            }
        });

        Button saveButton = new Button("Save", event -> {
            String newName = nameField.getValue();
            String newDescription = descriptionField.getValue();
            LocalDateTime newDeadline = deadlinePicker.getValue();

            if (newName.isEmpty()) {
                getCookie(String.valueOf(task.getId()), "task_name", savedName -> task.setName(savedName));
            } else {
                task.setName(newName);
            }
            if (newDescription.isEmpty()) {
                getCookie(String.valueOf(task.getId()), "task_description", savedDescription -> task.setDescription(savedDescription));
            } else {
                task.setDescription(newDescription);
            }
            if (newDeadline == null) {
                getCookie(String.valueOf(task.getId()), "task_deadline", savedDeadline -> {
                    if (savedDeadline != null && !savedDeadline.isEmpty()) {
                        task.setDeadline(LocalDateTime.parse(savedDeadline));
                    }
                });
            } else {
                task.setDeadline(newDeadline);
            }

            taskService.updateTask(task);
            taskGrid.getDataProvider().refreshItem(task);
            refreshGridItems();
            dialog.close();
        });

        Button deleteButton = new Button("Delete", event -> {
            taskService.deleteTask(task);
            refreshGridItems();
            dialog.close();
        });

        dialog.add(nameField, descriptionField, deadlinePicker, saveButton, deleteButton);
        dialog.open();
    }
}
