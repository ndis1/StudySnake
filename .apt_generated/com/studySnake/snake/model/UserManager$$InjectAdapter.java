// Code generated by dagger-compiler.  Do not edit.
package com.studySnake.snake.model;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<UserManager>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code UserManager} and its
 * dependencies.
 * 
 * Being a {@code Provider<UserManager>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<UserManager>} and handling injection
 * of annotated fields.
 */
public final class UserManager$$InjectAdapter extends Binding<UserManager>
    implements Provider<UserManager>, MembersInjector<UserManager> {
  private Binding<com.squareup.otto.Bus> parameter_bus;
  private Binding<com.squareup.otto.Bus> field_bus;

  public UserManager$$InjectAdapter() {
    super("com.studySnake.snake.model.UserManager", "members/com.studySnake.snake.model.UserManager", NOT_SINGLETON, UserManager.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    parameter_bus = (Binding<com.squareup.otto.Bus>) linker.requestBinding("com.squareup.otto.Bus", UserManager.class, getClass().getClassLoader());
    field_bus = (Binding<com.squareup.otto.Bus>) linker.requestBinding("com.squareup.otto.Bus", UserManager.class, getClass().getClassLoader());
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    getBindings.add(parameter_bus);
    injectMembersBindings.add(field_bus);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<UserManager>}.
   */
  @Override
  public UserManager get() {
    UserManager result = new UserManager(parameter_bus.get());
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<UserManager>}.
   */
  @Override
  public void injectMembers(UserManager object) {
    object.bus = field_bus.get();
  }
}
