package com.johnny.todo.Room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository{

    //Comentario só para dar commit com uma mensagem mais descritiva

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application){
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public long insert(Task task){
        long id;
        try {
            id =  new InsertTaskAsyncTask(taskDao).execute(task).get();
            return id;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public void update(Task task){
        new UpdateTaskAsyncTask(taskDao).execute(task);
    }

    public void delete(Task task){
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }

    public void deleteAllTasks(){
        new DeleteAllTasksTaskAsyncTask(taskDao).execute();
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Long>{
        private TaskDao taskDao;

        private  InsertTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Long doInBackground(Task... tasks) {
            return taskDao.insert(tasks[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void>{
        private TaskDao taskDao;

        private  UpdateTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void>{
        private TaskDao taskDao;

        private  DeleteTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllTasksTaskAsyncTask extends AsyncTask<Task, Void, Void>{
        private TaskDao taskDao;

        private  DeleteAllTasksTaskAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.deleteAllTasks();
            return null;
        }
    }
}
