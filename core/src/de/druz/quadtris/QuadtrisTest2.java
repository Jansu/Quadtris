package de.druz.quadtris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class QuadtrisTest2 extends ApplicationAdapter {
	Array<Viewport> viewports;
	Viewport viewport;
	Array<String> names;
	String name;

	private PerspectiveCamera camera;
	public Environment environment;
	public DirectionalLight shadowLight;
	public ModelBuilder modelBuilder;
	public ModelBatch modelBatch;
	public ModelInstance boxInstance;

	public void create() {
		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.3f,
				0.3f, 0.3f, 1.f));
		shadowLight = new DirectionalLight();
		shadowLight.set(0.8f, 0.8f, 0.8f, -0.5f, -1f, 0.7f);
		environment.add(shadowLight);

		modelBatch = new ModelBatch();

		camera = new PerspectiveCamera();
		camera.fieldOfView = 67;
		camera.near = 0.1f;
		camera.far = 300f;
		camera.position.set(0, 0, 100);
		camera.lookAt(0, 0, 0);

		viewports = getViewports(camera);
		viewport = viewports.first();

		names = getViewportNames();
		name = names.first();

		ModelBuilder modelBuilder = new ModelBuilder();
		Model boxModel = modelBuilder.createBox(50f, 50f, 50f, new Material(
				ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position
				| Usage.Normal);
		boxInstance = new ModelInstance(boxModel);
		boxInstance.transform.rotate(1, 0, 0, 30);
		boxInstance.transform.rotate(0, 1, 0, 30);

		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean keyDown(int keycode) {
				if (keycode == Input.Keys.SPACE) {
					int index = (viewports.indexOf(viewport, true) + 1)
							% viewports.size;
					name = names.get(index);
					viewport = viewports.get(index);
					resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				}
				return false;
			}
		});
	}

	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(camera);
		modelBatch.render(boxInstance, environment);
		modelBatch.end();
	}

	public void resize(int width, int height) {
		System.out.println(name);
		viewport.update(width, height);
	}

	static public Array<String> getViewportNames () {
		Array<String> names = new Array();
		names.add("StretchViewport");
		names.add("FillViewport");
		names.add("FitViewport");
		names.add("ExtendViewport: no max");
		names.add("ExtendViewport: max");
		names.add("ScreenViewport: 1:1");
		names.add("ScreenViewport: 0.75:1");
		names.add("ScalingViewport: none");
		return names;
	}

	static public Array<Viewport> getViewports (Camera camera) {
		int minWorldWidth = 640;
		int minWorldHeight = 480;
		int maxWorldWidth = 800;
		int maxWorldHeight = 480;

		Array<Viewport> viewports = new Array();
		viewports.add(new StretchViewport(minWorldWidth, minWorldHeight, camera));
		viewports.add(new FillViewport(minWorldWidth, minWorldHeight, camera));
		viewports.add(new FitViewport(minWorldWidth, minWorldHeight, camera));
		viewports.add(new ExtendViewport(minWorldWidth, minWorldHeight, camera));
		viewports.add(new ExtendViewport(minWorldWidth, minWorldHeight, maxWorldWidth, maxWorldHeight, camera));
		viewports.add(new ScreenViewport(camera));

		ScreenViewport screenViewport = new ScreenViewport(camera);
		screenViewport.setUnitsPerPixel(0.75f);
		viewports.add(screenViewport);

		viewports.add(new ScalingViewport(Scaling.none, minWorldWidth, minWorldHeight, camera));
		return viewports;
	}
}
