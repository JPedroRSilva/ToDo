package com.johnny.todo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.LocalDateTime;

import static com.johnny.todo.LocalDateTimeConverter.toDateString;

@Database(entities = {Task.class}, version = 2, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;

    public abstract TaskDao taskDao();

    public static synchronized TaskDatabase getInstance(Context context) {
        if(instance == null){
             instance = Room.databaseBuilder(context.getApplicationContext(),
                     TaskDatabase.class, "task_database")
                     .fallbackToDestructiveMigration()
                     .addCallback(roomCallback)
                     .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private TaskDao taskDao;

        private PopulateDbAsyncTask(TaskDatabase db){
            taskDao = db.taskDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            LocalDateTime time = LocalDateTime.now();
            taskDao.insert(new Task("Example" , "Description", toDateString(time), false));
            return null;
        }
    }
}
