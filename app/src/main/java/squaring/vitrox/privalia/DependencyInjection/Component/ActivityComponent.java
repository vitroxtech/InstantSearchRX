package squaring.vitrox.privalia.DependencyInjection.Component;

import dagger.Component;
import squaring.vitrox.privalia.DependencyInjection.ActivityScope;
import squaring.vitrox.privalia.MainActivity;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent{

    void inject(MainActivity activity);

}