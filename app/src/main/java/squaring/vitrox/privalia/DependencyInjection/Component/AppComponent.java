package squaring.vitrox.privalia.DependencyInjection.Component;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import javax.inject.Singleton;

import dagger.Component;
import squaring.vitrox.privalia.Common.PaginationHelper;
import squaring.vitrox.privalia.DependencyInjection.Module.ApplicationModule;
import squaring.vitrox.privalia.Network.ApiService;
import squaring.vitrox.privalia.Network.ServiceModule;
import squaring.vitrox.privalia.Network.TmbdService;

@Singleton
@Component(modules = {ApplicationModule.class, ServiceModule.class})
public interface AppComponent {

    Context appContext();

    ApiService apiService();

    TmbdService tmbdService();

    PaginationHelper paginationHelper();

}
