package squaring.vitrox.privalia.DependencyInjection.Module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import squaring.vitrox.privalia.App;
import squaring.vitrox.privalia.Common.PaginationHelper;

@Module
public class ApplicationModule {

    private final App mApp;

    public ApplicationModule(App app) {
        mApp = app;
    }

    @Provides
    @Singleton
    public Context appContext() {
        return mApp;
    }

    @Provides
    @Singleton
    public PaginationHelper paginationHelper()
    {
        return new PaginationHelper();
    }

}
