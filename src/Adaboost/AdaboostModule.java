package Adaboost;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class AdaboostModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AppController.class).in(Singleton.class);
        bind(DataController.class).in(Singleton.class);
        bind(DecisionTreeClassifierHelper.class).in(Singleton.class);
        bind(PercentageFormat.class).in(Singleton.class);

        bind(Logger.class).to(ConsoleLogger.class).in(Singleton.class);
    }

}
